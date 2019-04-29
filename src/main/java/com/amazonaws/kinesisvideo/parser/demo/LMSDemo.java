package com.amazonaws.kinesisvideo.parser.demo;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSSessionCredentialsProvider;
import com.amazonaws.kinesisvideo.parser.examples.LMSProcessor;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisvideo.*;
import com.amazonaws.services.kinesisvideo.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;


//{
//        "Credentials": {
//        "AccessKeyId": "ASIA324RKUG4HRJHH24W",
//        "SecretAccessKey": "oXzb4BKvmmATjMS6MvkoKcvs0axdMk8x55rankBi",
//        "SessionToken": "FQoGZXIvYXdzEFcaDDNUmwLyECyRHpvh1iKsAYP5+kxC2Ou+Y7THyOTSvIppfZGnr2rw61NrhEmrgyOrTKMS24FlWf6UQAMZXrvT5K1LTiLRX64g2eayAzSsjI4n9fbsKdACb4ns7fuGYrONN7iHa6M5IEqDixq3Z0JHlSVwoomqxoxcprsoJQ76641+eI1xDmD40aNaiJkfollEDs1KWRzaSu8hp3C6koM8uW4Os4Q8AithOFyInCbOicJfhFucqDyKPQsS4PIoyYfZ5QU=",
//        "Expiration": "2019-04-18T09:06:17Z"
//        }
//}

@Slf4j
public class LMSDemo {

    private static String SESSION_TOKEN = "FQoGZXIvYXdzEG4aDMt5ZKkcMvsso2MG+SKsAfxhI2THGc+vfMjxEISdHo5XUkGPgv1s5LwVc8hQ5TZCYACf3DYp+jYa+5sZUP00+dLu3DPVNFh2+dk++WKZkTe/18+dc5K83yuGNABQyXXuexmzvML23WTZmhwTq5hqB3rmbRbBclyjdWCJpNi2B7OuWV84LMfTPHWek6DOKqvpD6N1CvIjBzKXg6cN/4JZ8I3GRgPnhPzwvWuy7RPPsgUWC3EUywo84Kjcpaoos5Le5QU=";
    private static String SESSION_ACCESS_KEY_ID = "ASIA324RKUG4K7MDHXV2";
    private static String SESSION_SECRET_KEY = "zi+YeouKTfpV545qvMdYm8kBP7TFxD3bCUS7lvfW";

    public void run() throws InterruptedException, IOException {

        AWSSessionCredentialsProvider myCreds = new AWSSessionCredentialsProvider() {
            @Override
            public AWSSessionCredentials getCredentials() {
                return new AWSSessionCredentials() {
                    @Override
                    public String getSessionToken() {
                        return SESSION_TOKEN;
                    }

                    @Override
                    public String getAWSAccessKeyId() {
                        return SESSION_ACCESS_KEY_ID;
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return SESSION_SECRET_KEY;
                    }
                };
            }

            @Override
            public void refresh() {

            }
        };

        final AmazonKinesisVideoClientBuilder builder = AmazonKinesisVideoClientBuilder.standard();
        builder.withCredentials(myCreds).withRegion(Regions.US_EAST_1);

        AmazonKinesisVideo client = builder.build();
        ListStreamsRequest req = new ListStreamsRequest();
        ListStreamsResult streams = client.listStreams(req);

        log.info("First stream found: {}", streams.getStreamInfoList().get(0).getStreamName());

        // GetMediaWorker getMediaWorker = GetMediaWorker.create(Regions.US_EAST_1, myCreds, streams.getStreamInfoList().get(0).getStreamName(), new StartSelector.withStartSelectorType(StartSelectorType.EARLIEST), streamOps.amazonKinesisVideo, )

        // return;

        LMSProcessor example = new LMSProcessor(
            Regions.US_EAST_1,
            streams.getStreamInfoList().get(0).getStreamName(),
            // "91343852333181432392682062619333857297914307476",
            "adf",
            myCreds,
            new FileOutputStream("output.raw"));
        example.execute();
    }

}
