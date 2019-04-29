package io.slingr.endpoints.awsconnect;

import io.slingr.endpoints.utils.Json;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import uk.co.lucasweb.aws.v4.signer.HttpRequest;
import uk.co.lucasweb.aws.v4.signer.Signer;
import uk.co.lucasweb.aws.v4.signer.credentials.AwsCredentials;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AwsConnectUtils {

    private String region;
    private String accessKey;
    private String secretAccessKey;

    private SimpleDateFormat dateFormatter;

    public AwsConnectUtils(String region, String accessKey, String secretAccessKey) {
        this.region = region;
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;
        this.dateFormatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public String getBodyHash(Json body) {
        String text = "";
        if (body != null && !body.isEmpty()) {
            text = body.toString();
        }
        return DigestUtils.sha256Hex(text);
    }

    public String getExpirationDate(Date date) {
        date = new Date(date.getTime() + (1000 * 60 * 5));
        return dateFormatter.format(date);
    }

    public String getSignature(String method, String expirationDate, String path, String queryString, String bodyHash) {
        HttpRequest request = null;
        try {
            request = new HttpRequest(method, new URI("https://connect."+region+".amazonaws.com"+path+(StringUtils.isBlank(queryString) ? "" : "?"+queryString)));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error generating URI", e);
        }
        String signature = Signer.builder()
                .awsCredentials(new AwsCredentials(accessKey, secretAccessKey))
                .header("host", "connect."+region+".amazonaws.com")
                .header("x-amz-date", expirationDate)
                .header("x-amz-content-sha256", bodyHash)
                .build(request, "connect", bodyHash)
                .getSignature();
        return signature;
    }
}
