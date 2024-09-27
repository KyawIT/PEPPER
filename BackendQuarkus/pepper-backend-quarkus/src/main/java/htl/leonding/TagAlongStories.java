package htl.leonding;

import jakarta.ws.rs.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
@Path("/api")
public class TagAlongStories {

    @GET
    @Path("/tagalongstories")
    @Operation(summary = "Get all TagAlongStories")

    public String tagalongstories()
    {
        return "/tagalongstories";
    }

    @GET
    @Path("/tagalongstories/{id}/picture")
    public String tagalongstoriesPicById()
    {
        return "/tagalongstories/{id}/picture";
    }

    @GET
    @Path("/tagalongstories/{id}")
    public String tagalongstoriesById()
    {
        return "/tagalongstories/{id}";
    }

    @POST
    @Path("/tagalongstories")
    public String tagalongstoriesUpload()
    {
        return "Hello RESTEasy";
    }

    @PUT
    @Path("/tagalongstories/{id}")
    public String tagalongstoriesUpdateById()
    {
        return "Hello RESTEasy";
    }

    @DELETE
    @Path("/tagalongstories/{id}")
    public String tagalongstoriesDeleteById()
    {
        return "Hello RESTEasy";
    }

    @POST
    @Path("/tagalongstories/{id}/steps")
    public String tagalongstoriesUploadStepsById()
    {
        return "Hello RESTEasy";
    }

    @GET
    @Path("/tagalongstories/{id}/steps")
    public String tagalongstoriesStepsById()
    {
        return "/tagalongstories/{id}/steps";
    }
}
