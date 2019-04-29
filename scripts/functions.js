
endpoint.CreateUser = function(body) {
    return endpoint._httpPut({
        path: '/users/:instanceId',
        body: body
    });
};

endpoint.DeleteUser = function(userId) {
    return endpoint._httpDelete({
        path: '/users/:instanceId/'+userId
    });
};

endpoint.DescribeUser = function(userId) {
    return endpoint._httpGet({
        path: '/users/:instanceId/'+userId
    });
};

endpoint.DescribeUserHierarchyGroup = function(hierarchyGroupId) {
    return endpoint._httpGet({
        path: '/user-hierarchy-groups/:instanceId/'+hierarchyGroupId
    });
};

endpoint.DescribeUserHierarchyStructure = function() {
    return endpoint._httpGet({
        path: '/user-hierarchy-structure/:instanceId'
    });
};

endpoint.GetContactAttributes = function(initialContactId) {
    return endpoint._httpGet({
        path: '/contact/attributes/:instanceId/'+initialContactId
    });
};

endpoint.GetCurrentMetricData = function(body) {
    return endpoint._httpPost({
        path: '/metrics/current/:instanceId',
        body: body
    });
};

endpoint.GetFederationToken = function() {
    return endpoint._httpGet({
        path: '/user/federate/:instanceId'
    });
};

endpoint.GetMetricData = function(body) {
    return endpoint._httpPost({
        path: '/metrics/historical/:instanceId',
        body: body
    });
};

endpoint.ListRoutingProfiles = function(maxResults, nextToken) {
    return endpoint._httpGet({
        path: '/routing-profiles-summary/:instanceId',
        params: {
            maxResults: maxResults,
            nextToken: nextToken
        }
    });
};

endpoint.ListSecurityProfiles = function(maxResults, nextToken) {
    return endpoint._httpGet({
        path: '/security-profiles-summary/:instanceId',
        params: {
            maxResults: maxResults,
            nextToken: nextToken
        }
    });
};

endpoint.ListUserHierarchyGroups = function(maxResults, nextToken) {
    return endpoint._httpGet({
        path: '/user-hierarchy-groups-summary/:instanceId',
        params: {
            maxResults: maxResults,
            nextToken: nextToken
        }
    });
};

endpoint.ListUsers = function(maxResults, nextToken) {
    return endpoint._httpGet({
        path: '/users-summary/:instanceId',
        params: {
            maxResults: maxResults,
            nextToken: nextToken
        }
    });
};

endpoint.StartOutboundVoiceContact = function(body) {
    body.InstanceId = endpoint._configuration.instanceId;
    return endpoint._httpPut({
        path: '/contact/outbound-voice',
        body: body
    });
};

endpoint.StopContact = function(body) {
    body.InstanceId = endpoint._configuration.instanceId;
    return endpoint._httpPost({
        path: '/users/:instanceId',
        body: body
    });
};

endpoint.UpdateContactAttributes = function(body) {
    body.InstanceId = endpoint._configuration.instanceId;
    return endpoint._httpPost({
        path: '/contact/attributes',
        body: body
    });
};

endpoint.UpdateUserHierarchy = function(userId, body) {
    return endpoint._httpPost({
        path: '/users/:instanceId/'+userId+'/hierarchy',
        body: body
    });
};

endpoint.UpdateUserIdentityInfo = function(userId, body) {
    return endpoint._httpPost({
        path: '/users/:instanceId/'+userId+'/identity-info',
        body: body
    });
};

endpoint.UpdateUserPhoneConfig = function(userId, body) {
    return endpoint._httpPost({
        path: '/users/:instanceId/'+userId+'/phone-config',
        body: body
    });
};

endpoint.UpdateUserRoutingProfile = function(userId, body) {
    return endpoint._httpPost({
        path: '/users/:instanceId/'+userId+'/routing-profile',
        body: body
    });
};

endpoint.UpdateUserSecurityProfiles = function(userId, body) {
    return endpoint._httpPost({
        path: '/users/:instanceId/'+userId+'/security-profiles',
        body: body
    });
};