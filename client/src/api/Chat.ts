import { fetchJSONAuth } from "./utils";
import { ChatsToResp, ChatToResp, TChatsToResp, TChatToResp } from "./dto";

export async function getMyChats(): Promise<TChatsToResp> {
    return fetchJSONAuth("/chat/my", "GET", ChatsToResp);
}

export async function getChat(id: number): Promise<TChatToResp> {
    return fetchJSONAuth("/chat/by-id/" + id, "GET", ChatToResp);
}

export async function createChat(
    name: string,
    memberUuids: string[],
): Promise<TChatToResp> {
    return fetchJSONAuth("/chat", "POST", ChatToResp, { name, memberUuids });
}
