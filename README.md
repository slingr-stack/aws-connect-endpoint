---
title: AWS Connect endpoint
keywords: 
last_updated: January 10, 2019
tags: []
summary: "Detailed description of the API of the AWS Connect endpoint."
---

## Overview

The AWS Connect endpoint has the following features:
 
- Authentication
- Automatic signing of request
- Helpers to use the REST API

Please make sure you take a look at the documentation from AWS Connect as features are based on its API:

- [API Reference](https://docs.aws.amazon.com/connect/latest/APIReference/Welcome.html)

## Quick start

First of all you will need to create an instance for AWS Connect. Once you have created it and the
endpoint is configured, you can start making calls to the API. For example you can list users (up
to 10) like this:

```js
var users = app.endpoints.awsconnect.ListUsers(10);
log('users: ' + JSON.stringify(users));
```

Or you can update the profile of a user like this:

```js
app.endpoints.proxy.UpdateUserIdentityInfo('73793cda-6179-48b9-af72-7a6745eaedef', {
  IdentityInfo: {
    Email: 'user@test.com',
    FirstName: 'User',
    LastName: 'Test'
  }
});
```

## Configuration

In order to configure the endpoint, you previously need to have an AWS account and an instance
created for AWS Connect.

### AWS Access Key

The access key for the user making the calls. Make sure the user has access to your AWS Connect instance.

### AWS Secret Access Key

The secret access key for the user making the calls. Make sure the user has access to your AWS Connect instance.

### Region

The region where your AWS Connect instance was created. For example `us-east-1`.

### Instance ID

The ID of your AWS Connect instance.

## Javascript API

### REST API shortcuts

The endpoint provides methods to access all methods in the [REST API](https://docs.aws.amazon.com/connect/latest/APIReference/Welcome.html).
Here is a list of the available methods in the API, but you should check AWS Connect's documentation for more
information:

```js
app.endpoints.awsconnect.CreateUser(body);
app.endpoints.awsconnect.DeleteUser(userId);
app.endpoints.awsconnect.DescribeUser(userId);
app.endpoints.awsconnect.DescribeUserHierarchyGroup(hierarchyGroupId);
app.endpoints.awsconnect.DescribeUserHierarchyStructure();
app.endpoints.awsconnect.GetContactAttributes(initialContactId);
app.endpoints.awsconnect.GetCurrentMetricData(body);
app.endpoints.awsconnect.GetFederationToken();
app.endpoints.awsconnect.GetMetricData(body);
app.endpoints.awsconnect.ListRoutingProfiles(maxResults, nextToken);
app.endpoints.awsconnect.ListSecurityProfiles(maxResults, nextToken);
app.endpoints.awsconnect.ListUserHierarchyGroups(maxResults, nextToken);
app.endpoints.awsconnect.ListUsers(maxResults, nextToken);
app.endpoints.awsconnect.StartOutboundVoiceContact(body);
app.endpoints.awsconnect.StopContact(body);
app.endpoints.awsconnect.UpdateContactAttributes(body);
app.endpoints.awsconnect.UpdateUserHierarchy(userId, body);
app.endpoints.awsconnect.UpdateUserIdentityInfo(userId, body);
app.endpoints.awsconnect.UpdateUserPhoneConfig(userId, body);
app.endpoints.awsconnect.UpdateUserRoutingProfile(userId, body);
app.endpoints.awsconnect.UpdateUserSecurityProfiles(userId, body);
```

## Helpers

In order to work with Amazon Kinesis Video Streams following helpers are available:

```js
app.endpoints.awsconnect.kinesis.listStreams(bodyParams);
app.endpoints.awsconnect.kinesis.getStream(streamName, callbackData, callbacks);
app.endpoints.awsconnect.kinesis.deleteStream(streamARN);
app.endpoints.awsconnect.kinesis.request(path, bodyParams);
```

Check [Amazon Kinesis Video Streams - API Reference](https://docs.aws.amazon.com/kinesisvideostreams/latest/dg/API_Operations_Amazon_Kinesis_Video_Streams.html) to get more information.

To retrieve all streams we can

```js
var res = app.endpoints.awsConnect.kinesis.listStreams();
log(JSON.stringify(res));
```

We can retrieve an stream by name

```js
var res = app.endpoints.awsConnect.kinesis.getStream(action.field('streamName').val(), 
{ record: record }, 
{
    'streamResponse': function(res, callbackData) {
        var data = res.data;
        if(data && data.status == "ok") {
            var document = callbackData.record;
            document.field('streamFile').val({
                id: data.file.fileId,
                name: data.file.fileName,
                contentType: data.file.contentType
            });
            sys.data.save(document);
        }
    }
});
```

An example to do a kinesis request

```js
var res = app.endpoints.awsConnect.kinesis.request('describeStream', {
  'StreamARN': 'arn:aws:kinesisvideo:us-east-1:813664215480:stream/connect-streams-connect-t9bot-dev-contact-f6bb8c88-f13b-4eb6-bf26-5413cdf5be05/1555532662805'
});
log(JSON.stringify(res));
```

## Events

This endpoint does not have any event.

## About SLINGR

SLINGR is a low-code rapid application development platform that accelerates development, with robust architecture for integrations and executing custom workflows and automation.

[More info about SLINGR](https://slingr.io)

## License

This endpoint is licensed under the Apache License 2.0. See the `LICENSE` file for more details.
