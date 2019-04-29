package com.amazonaws.kinesisvideo.parser.utilities;

import com.amazonaws.kinesisvideo.parser.mkv.Frame;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class LMSFrameProcessor implements FrameVisitor.FrameProcessor {

    private OutputStream outputStream;


    protected LMSFrameProcessor(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public static LMSFrameProcessor create(OutputStream outputStream) {
        return new LMSFrameProcessor(outputStream);
    }

    @Override
    public void process(Frame frame, MkvTrackMetadata trackMetadata) {
        saveToOutPutStream(frame);
    }

    private void saveToOutPutStream(final Frame frame) {
        ByteBuffer frameBuffer = frame.getFrameData();
        try {
            byte[] frameBytes = new byte[frameBuffer.remaining()];
            frameBuffer.get(frameBytes);
            outputStream.write(frameBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
