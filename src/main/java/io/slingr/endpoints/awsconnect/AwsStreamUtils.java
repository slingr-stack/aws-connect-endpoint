package io.slingr.endpoints.awsconnect;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.kinesisvideo.parser.examples.LMSProcessor;
import com.amazonaws.regions.Regions;
import io.slingr.endpoints.services.Files;
import io.slingr.endpoints.utils.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

public class AwsStreamUtils {

    private static final Logger logger = LoggerFactory.getLogger(AwsStreamUtils.class);

    private String region;
    private String accessKey;
    private String secretAccessKey;
    private Files files;

    public AwsStreamUtils(String region, String accessKey, String secretAccessKey, Files files) {
        this.region = region;
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;
        this.files = files;
    }

    public Json downloadFile(String streamName) {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretAccessKey);
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCreds);

        final String fragmentNumber = "adf";
        logger.info(String.format("Finding stream [%s]", streamName));

        try {

            File temp = File.createTempFile("stream-" + new Date().getTime(), ".raw");
            FileOutputStream outputStream = new FileOutputStream(temp);

            LMSProcessor example = new LMSProcessor(
                    Regions.fromName(region),
                    streamName,
                    fragmentNumber,
                    credentialsProvider,
                    outputStream);
            example.execute();

            // https://console.bluemix.net/docs/services/speech-to-text/audio-formats.html#audio-formats
            Json fileJson = files.upload(temp.getName(), new FileInputStream(temp), "audio/l16");

            return fileJson;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;

    }

}
