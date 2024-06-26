import { number, z } from "zod";

export const ErrorTo = z.object({
    errors: z.array(z.string()),
    code: z.number(),
});
export type TErrorTo = z.infer<typeof ErrorTo>;

export function isError(value: unknown): value is TErrorTo {
    return ErrorTo.safeParse(value).success;
}

function CreateAPIResponse<T extends z.ZodTypeAny>(obj: T) {
    return z.union([ErrorTo, obj]);
}

export const NoContentTo = z.object({});
export type TNoContentTo = z.infer<typeof NoContentTo>;

export const NoContentToResp = CreateAPIResponse(NoContentTo);
export type TNoContentToResp = z.infer<typeof NoContentToResp>;

export const PersonSignupTo = z.object({
    username: z.string(),
    fullName: z.string(),
    password: z.string(),
});
export type TPersonSignupTo = z.infer<typeof PersonSignupTo>;

export const PersonTo = z.object({
    uuid: z.string(),
    username: z.string(),
    fullName: z.string(),
    isAdmin: z.boolean(),
});
export type TPersonTo = z.infer<typeof PersonTo>;

export const PersonToResp = CreateAPIResponse(PersonTo);
export type TPersonToResp = z.infer<typeof PersonToResp>;

export const PersonToArr = z.array(PersonTo);
export type TPersonToArr = z.infer<typeof PersonToArr>;

export const PersonToArrResp = CreateAPIResponse(PersonToArr);
export type TPersonToArrResp = z.infer<typeof PersonToArrResp>;

export const TokenRequestTo = z.object({
    username: z.string(),
    password: z.string(),
});
export type TTokenRequestTo = z.infer<typeof TokenRequestTo>;

export const TokenTo = z.object({
    token: z.string(),
});
export type TTokenTo = z.infer<typeof TokenTo>;

export const TokenToResp = CreateAPIResponse(TokenTo);
export type TTokenToResp = z.infer<typeof TokenToResp>;

export const PostTo = z.object({
    id: z.number(),
    authorUuid: z.string(),
    text: z.string(),
    createdAt: z.number(),
    authorUsername: z.string(),
});
export type TPostTo = z.infer<typeof PostTo>;

export const PostToArr = z.array(PostTo);
export type TPostToArr = z.infer<typeof PostToArr>;

export const PostToResp = CreateAPIResponse(PostTo);
export type TPostToResp = z.infer<typeof PostToResp>;

export const PostToArrResp = CreateAPIResponse(PostToArr);
export type TPostToArrResp = z.infer<typeof PostToArrResp>;

export const MessageTo = z.object({
    id: number(),
    chatId: z.number(),
    authorUuid: z.string(),
    authorUsername: z.string(),
    contents: z.string(),
    createdAt: z.number(),
});
export type TMessageTo = z.infer<typeof MessageTo>;

export const MessageToResp = CreateAPIResponse(MessageTo);
export type TMessageToResp = z.infer<typeof MessageToResp>;

export const MessagesToResp = CreateAPIResponse(z.array(MessageTo));
export type TMessagesToResp = z.infer<typeof MessagesToResp>;

export const ChatTo = z.object({
    id: z.number(),
    name: z.string(),
    creatorUuid: z.string(),
    memberCount: z.number(),
});
export type TChatTo = z.infer<typeof ChatTo>;

export const ChatToResp = CreateAPIResponse(ChatTo);
export type TChatToResp = z.infer<typeof ChatToResp>;

export const ChatsToResp = CreateAPIResponse(z.array(ChatTo));
export type TChatsToResp = z.infer<typeof ChatsToResp>;
