package io.slingr.endpoints.awsconnect;

import io.slingr.endpoints.HttpEndpoint;
import io.slingr.endpoints.framework.annotations.ApplicationLogger;
import io.slingr.endpoints.framework.annotations.EndpointFunction;
import io.slingr.endpoints.framework.annotations.EndpointProperty;
import io.slingr.endpoints.framework.annotations.SlingrEndpoint;
import io.slingr.endpoints.services.AppLogs;
import io.slingr.endpoints.utils.Json;
import io.slingr.endpoints.utils.Strings;
import io.slingr.endpoints.ws.exchange.FunctionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Executors;

/**
 * <p>Twilio endpoint
 *
 * <p>Created by dgaviola on 11/05/17.
 */
@SlingrEndpoint(name = "aws-connect")
public class AwsConnectEndpoint extends HttpEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(AwsConnectEndpoint.class);

    private final String REQ_PARAM_STREAM_NAME = "streamName";
    private final String REQ_PARAM_BODY = "body";
    private final String REQ_PARAM_PATH = "path";

    @ApplicationLogger
    private AppLogs appLogger;

    @EndpointProperty
    private String accessKey;

    @EndpointProperty
    private String secretAccessKey;

    @EndpointProperty
    private String region;

    @EndpointProperty
    private String instanceId;

    AwsConnectUtils awsConnectUtils;

    public AwsConnectEndpoint() {
    }

    @Override
    public String getApiUri() {
        return "https://connect." + region + ".amazonaws.com";
    }

    @Override
    public void endpointStarted() {
        awsConnectUtils = new AwsConnectUtils(region, accessKey, secretAccessKey);
    }

    @EndpointFunction(name = "_httpGet")
    public Json httpGet(FunctionRequest functionRequest) {
        String contentHash = awsConnectUtils.getBodyHash(null);
        String date = awsConnectUtils.getExpirationDate(new Date());
        Json functionParams = functionRequest.getJsonParams();
        String path = functionParams.string("path");
        path = path.replace(":instanceId", instanceId);
        Json requestParams = functionParams.json("params");
        String queryString = Strings.urlDecode(Strings.convertToQueryString(requestParams));
        String signature = awsConnectUtils.getSignature("GET", date, path, queryString, contentHash);
        Json request = Json.map()
                .set("path", path)
                .set("headers", Json.map()
                        .set("Authorization", signature)
                        .set("x-amz-date", date)
                        .set("x-amz-content-sha256", contentHash)
                ).set("params", requestParams);
        return httpService().defaultGetRequest(request);
    }

    @EndpointFunction(name = "_httpDelete")
    public Json httpDelete(FunctionRequest functionRequest) {
        String contentHash = awsConnectUtils.getBodyHash(null);
        String date = awsConnectUtils.getExpirationDate(new Date());
        Json functionParams = functionRequest.getJsonParams();
        String path = functionParams.string("path");
        path = path.replace(":instanceId", instanceId);
        Json requestParams = functionParams.json("params");
        String queryString = Strings.urlDecode(Strings.convertToQueryString(requestParams));
        String signature = awsConnectUtils.getSignature("DELETE", date, path, queryString, contentHash);
        Json request = Json.map()
                .set("path", path)
                .set("headers", Json.map()
                        .set("Authorization", signature)
                        .set("x-amz-date", date)
                        .set("x-amz-content-sha256", contentHash)
                ).set("params", requestParams);
        return httpService().defaultDeleteRequest(request);
    }

    @EndpointFunction(name = "_httpPost")
    public Json httpPost(FunctionRequest functionRequest) {
        Json functionParams = functionRequest.getJsonParams();
        Json body = functionParams.json("body");
        String contentHash = awsConnectUtils.getBodyHash(body);
        String date = awsConnectUtils.getExpirationDate(new Date());
        String path = functionParams.string("path");
        path = path.replace(":instanceId", instanceId);
        Json requestParams = functionParams.json("params");
        String queryString = Strings.urlDecode(Strings.convertToQueryString(requestParams));
        String signature = awsConnectUtils.getSignature("POST", date, path, queryString, contentHash);
        Json request = Json.map()
                .set("path", path)
                .set("headers", Json.map()
                        .set("Authorization", signature)
                        .set("x-amz-date", date)
                        .set("x-amz-content-sha256", contentHash)
                )
                .set("params", requestParams)
                .set("body", body);
        return httpService().defaultPostRequest(request);
    }

    @EndpointFunction(name = "_httpPut")
    public Json httpPut(FunctionRequest functionRequest) {
        Json functionParams = functionRequest.getJsonParams();
        Json body = functionParams.json("body");
        String contentHash = awsConnectUtils.getBodyHash(body);
        String date = awsConnectUtils.getExpirationDate(new Date());
        String path = functionParams.string("path");
        path = path.replace(":instanceId", instanceId);
        Json requestParams = functionParams.json("params");
        String queryString = Strings.urlDecode(Strings.convertToQueryString(requestParams));
        String signature = awsConnectUtils.getSignature("PUT", date, path, queryString, contentHash);
        Json request = Json.map()
                .set("path", path)
                .set("headers", Json.map()
                        .set("Authorization", signature)
                        .set("x-amz-date", date)
                        .set("x-amz-content-sha256", contentHash)
                )
                .set("params", requestParams)
                .set("body", body);
        return httpService().defaultPutRequest(request);
    }

    @EndpointFunction(name = "_kinesisRequest")
    public Json kinesisRequest(FunctionRequest request) {

        Json requestJsonParams = request.getJsonParams();
        String path = null;
        if (requestJsonParams.contains(REQ_PARAM_PATH)) {
            path = requestJsonParams.string(REQ_PARAM_PATH);
        }
        Json body = null;
        if (requestJsonParams.contains(REQ_PARAM_BODY)) {
            body = requestJsonParams.json("body");
        }
        AwsKinesisVideoRequest awsKinesisVideoRequest = new AwsKinesisVideoRequest(region, accessKey, secretAccessKey);
        return awsKinesisVideoRequest.requestAwsSdk(path, body);

    }

    @EndpointFunction(name = "_getStream")
    public Json getStream(FunctionRequest request) {

        Json resp = Json.map();
        Json body = request.getJsonParams();

        if (body.contains(REQ_PARAM_STREAM_NAME)) {

            final String streamId = body.string(REQ_PARAM_STREAM_NAME);

            Executors.newSingleThreadScheduledExecutor().execute(() -> {

                try {

                    AwsStreamUtils awsStreamUtils = new AwsStreamUtils(region, accessKey, secretAccessKey, files());
                    Json fileJson = awsStreamUtils.downloadFile(streamId);

                    resp.set("status", "ok");
                    resp.set("file", fileJson);

                    events().send("streamResponse", resp, request.getFunctionId());

                } catch (Exception e) {
                    appLogger.warn("Error when get stream", e);
                    resp.set("status", "fail");
                    resp.set("message", e.toString());

                    events().send("streamResponse", resp, request.getFunctionId());
                }
            });

        }

        resp.set("status", "ok");
        return resp;

    }

}
