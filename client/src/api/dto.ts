import { z } from "zod";

export const ErrorTo = z.object({
    errors: z.array(z.string()),
    code: z.number(),
});
export type TErrorTo = z.infer<typeof ErrorTo>;

function CreateAPIResponse<T extends z.ZodTypeAny>(obj: T) {
    return z.union([ErrorTo, obj]);
}

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
});
export type TPersonTo = z.infer<typeof PersonTo>;

export const PersonToResp = CreateAPIResponse(PersonTo);
export type TPersonToResp = z.infer<typeof PersonToResp>;

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

export function isError(value: unknown): value is TErrorTo {
    return ErrorTo.safeParse(value).success;
}
