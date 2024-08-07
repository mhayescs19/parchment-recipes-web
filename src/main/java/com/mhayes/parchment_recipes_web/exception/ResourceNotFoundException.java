package com.mhayes.parchment_recipes_web.exception;

import com.mhayes.parchment_recipes_web.dto.enums.Resource;

/** Instance of a findById request with an non-existing resource ID
 *
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Resource resourceType, Long resourceId) {
        super(resourceType.name() + " not found for id = " + resourceId);
    }

    public ResourceNotFoundException(Long resourceId) {
        super("Resource not found for id = " + resourceId);
    }
}
