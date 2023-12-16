import { TokenToResp, TTokenToResp } from "./dto";
import { fetchJSON } from "./utils";

export async function login(
    username: string,
    password: string,
): Promise<TTokenToResp> {
    return fetchJSON("/token", "POST", TokenToResp, {
        username,
        password,
    });
}
