import {
    MessagesToResp,
    MessageToResp,
    NoContentToResp,
    TMessagesToResp,
    TMessageToResp,
    TNoContentToResp,
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

export async function editMessage(
    messageId: number,
    messageContents: string,
): Promise<TMessageToResp> {
    return fetchJSONAuth(
        "/message/by-id/" + messageId,
        "PATCH",
        MessageToResp,
        {
            contents: messageContents,
        },
    );
}

export async function deleteMessage(
    messageId: number,
): Promise<TNoContentToResp> {
    return fetchJSONAuth(
        "/message/by-id/" + messageId,
        "DELETE",
        NoContentToResp,
    );
}
