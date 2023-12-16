import { PostToArrResp, PostToResp, TPostToArrResp, TPostToResp } from "./dto";
import { fetchJSONAuth } from "./utils";

export async function post(text: string): Promise<TPostToResp> {
    return fetchJSONAuth("/post", "POST", PostToResp, {
        text,
    });
}

export async function getPosts(author: string): Promise<TPostToArrResp> {
    return fetchJSONAuth(`/post?author=${author}`, "GET", PostToArrResp);
}
