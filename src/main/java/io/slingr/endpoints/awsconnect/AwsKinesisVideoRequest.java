package io.slingr.endpoints.awsconnect;

import com.amazonaws.*;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.http.*;
import com.amazonaws.util.IOUtils;
import io.slingr.endpoints.utils.Json;
import org.eclipse.jetty.http.HttpHeader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class AwsKinesisVideoRequest {

    private static final String SERVICE_NAME = "kinesisvideo";

    private String region;
    private String accessKey;
    private String secretAccessKey;

    public AwsKinesisVideoRequest(String region, String accessKey, String secretAccessKey) {
        this.region = region;
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;

    }

    public Json requestAwsSdk(String path, Json body) {

        final String SERVICE_URI = "https://" + SERVICE_NAME + "." + region + ".amazonaws.com";

        String fullPath = SERVICE_URI + path;

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);

        //Instantiate the request
        Request<Void> request = new DefaultRequest<>(SERVICE_NAME);
        request.setHttpMethod(HttpMethodName.POST);
        request.setEndpoint(URI.create(fullPath));

        if(body != null) {
            InputStream is = new ByteArrayInputStream(body.toString().getBytes());
            request.setContent(is);
        }

        //Sign it...
        AWS4Signer signer = new AWS4Signer();
        signer.setRegionName(region);
        signer.setServiceName(request.getServiceName());
        signer.sign(request, awsCredentials);

        ClientConfiguration configuration = new ClientConfiguration();
        configuration.addHeader(HttpHeader.CONTENT_TYPE.asString(), "application/json;charset=UTF-8");
        configuration.addHeader(HttpHeader.ACCEPT.asString(), "application/json");

        //Execute it and get the response...
        Response<AmazonWebServiceResponse<String>> response = new AmazonHttpClient(configuration)
                .requestExecutionBuilder()
                .executionContext(new ExecutionContext(true))
                .request(request)
                .errorResponseHandler(new ErrorResponseHandler())
                .execute(new StringResponseHandler());

        return Json.parse(response.getAwsResponse().getResult());

    }


    public class StringResponseHandler implements HttpResponseHandler<AmazonWebServiceResponse<String>> {

        @Override
        public AmazonWebServiceResponse<String> handle(com.amazonaws.http.HttpResponse response) throws IOException {

            AmazonWebServiceResponse<String> awsResponse = new AmazonWebServiceResponse<>();

            //putting response string in the result, available outside the handler
            awsResponse.setResult(IOUtils.toString(response.getContent()));

            return awsResponse;
        }

        @Override
        public boolean needsConnectionLeftOpen() {
            return false;
        }

    }


    public class ErrorResponseHandler implements HttpResponseHandler<SdkBaseException> {

        @Override
        public SdkBaseException handle(HttpResponse response) throws Exception {

            SdkBaseException sdkBaseException = new AmazonServiceException(IOUtils.toString(response.getContent()));
            ((AmazonServiceException) sdkBaseException).setStatusCode(response.getStatusCode());

            return sdkBaseException;
        }

        @Override
        public boolean needsConnectionLeftOpen() {
            return false;
        }
    }

}
