/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.transform.rest.action.compat;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.RestApiVersion;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;
import org.elasticsearch.xpack.core.action.util.PageParams;
import org.elasticsearch.xpack.core.transform.TransformField;
import org.elasticsearch.xpack.core.transform.TransformMessages;
import org.elasticsearch.xpack.core.transform.action.GetTransformAction;
import org.elasticsearch.xpack.core.transform.action.compat.GetTransformActionDeprecated;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.xpack.core.transform.TransformField.ALLOW_NO_MATCH;
import static org.elasticsearch.xpack.core.transform.TransformField.EXCLUDE_GENERATED;

public class RestGetTransformActionDeprecated extends BaseRestHandler {
    @Override
    public List<Route> routes() {
        return List.of(
            Route.builder(GET, TransformField.REST_BASE_PATH_TRANSFORMS_DEPRECATED)
                .deprecated(TransformMessages.REST_DEPRECATED_ENDPOINT, RestApiVersion.V_8).build(),
            Route.builder(GET, TransformField.REST_BASE_PATH_TRANSFORMS_BY_ID_DEPRECATED)
                .deprecated(TransformMessages.REST_DEPRECATED_ENDPOINT, RestApiVersion.V_8).build()
        );
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient client) {
        GetTransformAction.Request request = new GetTransformAction.Request();

        String id = restRequest.param(TransformField.ID.getPreferredName());
        request.setResourceId(id);
        request.setAllowNoResources(restRequest.paramAsBoolean(ALLOW_NO_MATCH.getPreferredName(), true));
        if (restRequest.hasParam(PageParams.FROM.getPreferredName()) || restRequest.hasParam(PageParams.SIZE.getPreferredName())) {
            request.setPageParams(
                new PageParams(restRequest.paramAsInt(PageParams.FROM.getPreferredName(), PageParams.DEFAULT_FROM),
                    restRequest.paramAsInt(PageParams.SIZE.getPreferredName(), PageParams.DEFAULT_SIZE)));
        }
        return channel -> client.execute(GetTransformActionDeprecated.INSTANCE, request, new RestToXContentListener<>(channel));
    }

    @Override
    public String getName() {
        return "data_frame_get_transforms_action";
    }

    @Override
    protected Set<String> responseParams() {
        return Collections.singleton(EXCLUDE_GENERATED);
    }
}
