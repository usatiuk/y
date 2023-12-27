import { fetchJSONAuth } from "./utils";
import {
    ChatsToResp,
    ChatToResp,
    NoContentToResp,
    PersonToArrResp,
    TChatsToResp,
    TChatToResp,
    TNoContentToResp,
    TPersonToArrResp,
} from "./dto";

export async function getMyChats(): Promise<TChatsToResp> {
    return fetchJSONAuth("/chat/my", "GET", ChatsToResp);
}

export async function getChat(id: number): Promise<TChatToResp> {
    return fetchJSONAuth("/chat/by-id/" + id, "GET", ChatToResp);
}

export async function getMembers(id: number): Promise<TPersonToArrResp> {
    return fetchJSONAuth(
        "/chat/by-id/" + id + "/members",
        "GET",
        PersonToArrResp,
    );
}

export async function createChat(
    name: string,
    memberUuids: string[],
): Promise<TChatToResp> {
    return fetchJSONAuth("/chat", "POST", ChatToResp, { name, memberUuids });
}

export async function updateChat(
    id: number,
    name: string,
    memberUuids: string[],
): Promise<TChatToResp> {
    return fetchJSONAuth("/chat/by-id/" + id, "PATCH", ChatToResp, {
        name,
        memberUuids,
    });
}

export async function deleteChat(id: number): Promise<TNoContentToResp> {
    return fetchJSONAuth("/chat/by-id/" + id, "DELETE", NoContentToResp);
}
