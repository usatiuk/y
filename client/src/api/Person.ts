import { fetchJSON, fetchJSONAuth } from "./utils";
import { PersonToResp, TPersonToResp } from "./dto";

export async function signup(
    username: string,
    fullName: string,
    password: string,
): Promise<TPersonToResp> {
    return fetchJSON("/person", "POST", PersonToResp, {
        username,
        fullName,
        password,
    });
}

export async function getSelf(): Promise<TPersonToResp> {
    return fetchJSONAuth("/person", "GET", PersonToResp);
}

export async function getPersonByUsername(
    username: string,
): Promise<TPersonToResp> {
    return fetchJSONAuth("/person/" + username, "GET", PersonToResp);
}
