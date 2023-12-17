import {
    NoContentToResp,
    PostToArrResp,
    PostToResp,
    TNoContentToResp,
    TPostToArrResp,
    TPostToResp,
} from "./dto";
import { fetchJSONAuth } from "./utils";

export async function createPost(text: string): Promise<TPostToResp> {
    return fetchJSONAuth("/post", "POST", PostToResp, {
        text,
    });
}
export async function deletePost(id: number): Promise<TNoContentToResp> {
    return fetchJSONAuth(`/post/${id.toString()}`, "DELETE", NoContentToResp);
}

export async function getPostsByAuthorUuid(
    author: string,
): Promise<TPostToArrResp> {
    return fetchJSONAuth(
        `/post/by-author-uuid?author=${author}`,
        "GET",
        PostToArrResp,
    );
}

export async function getPostsByAuthorUsername(
    author: string,
): Promise<TPostToArrResp> {
    return fetchJSONAuth(
        `/post/by-author-username?author=${author}`,
        "GET",
        PostToArrResp,
    );
}
