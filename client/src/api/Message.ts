import {
    MessagesToResp,
    MessageToResp,
    TMessagesToResp,
    TMessageToResp,
} from "./dto";
import { fetchJSONAuth } from "./utils";

export async function getMessagesByChat(
    chatId: number,
): Promise<TMessagesToResp> {
    return fetchJSONAuth("/message/by-chat/" + chatId, "GET", MessagesToResp);
}

export async function addMessagesToChat(
    chatId: number,
    messageContents: string,
): Promise<TMessageToResp> {
    return fetchJSONAuth("/message/by-chat/" + chatId, "POST", MessageToResp, {
        contents: messageContents,
    });
}
