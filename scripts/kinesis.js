
endpoint.kinesis = {};

endpoint.kinesis.listStreams = function(bodyParams) {

    var config = {
        path: '/listStreams'
    };

    if(bodyParams) {
        config.body = bodyParams;
    }

    return endpoint._kinesisRequest(config);
};

endpoint.kinesis.getStream = function(streamName, callbackData, callbacks) {

    var params = {
        streamName: streamName
    };

    return endpoint._getStream(params, callbackData, callbacks);
};

endpoint.kinesis.deleteStream = function(streamARN) {

    var config = {
        path: '/deleteStream'
    };

    config.body = {
        StreamARN: streamARN
    };

    return endpoint._kinesisRequest(config);
};

endpoint.kinesis.request = function(path, bodyParams) {

    var config = {
        path: path.startsWith('/') ? path : '/' + path
    };

    if(bodyParams) {
        config.body = bodyParams;
    }

    return endpoint._kinesisRequest(config);
};