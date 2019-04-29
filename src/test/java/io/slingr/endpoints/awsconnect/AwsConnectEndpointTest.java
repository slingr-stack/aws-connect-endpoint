package io.slingr.endpoints.awsconnect;

import io.slingr.endpoints.utils.Json;
import io.slingr.endpoints.utils.tests.EndpointTests;
import io.slingr.endpoints.ws.exchange.FunctionRequest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@Ignore("For dev purposes only")
public class AwsConnectEndpointTest {

    private static EndpointTests test;
    private static AwsConnectEndpoint endpoint;

    @BeforeClass
    public static void init() throws Exception {
        test = EndpointTests.start(new io.slingr.endpoints.awsconnect.Runner(), "test.properties");
        endpoint = (AwsConnectEndpoint) test.getEndpoint();
    }

    @Test
    public void testGet() {
        Json functionParams = Json.map()
                .set("path", "/users-summary/:instanceId");
        FunctionRequest request = new FunctionRequest(Json.map().set("params", functionParams));
        Json res = endpoint.httpGet(request);
        System.out.println(res.toString());
        assertNotNull(res);
    }

    @Test
    public void testPost() {
        Json functionParams = Json.map()
                .set("path", "/users/:instanceId/73793cda-6179-48b9-af72-7a6745eaedef/identity-info")
                .set("body", Json.map()
                        .set("IdentityInfo", Json.map()
                                .set("Email", "dgaviola@slingr.io")
                                .set("FirstName", "Diego")
                                .set("LastName", "Gaviola")
                        )
                );
        FunctionRequest request = new FunctionRequest(Json.map().set("params", functionParams));
        Json res = endpoint.httpPost(request);
        System.out.println(res.toString());
        assertNotNull(res);
    }
}
