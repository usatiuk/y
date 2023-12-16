import { fetchJSON, fetchJSONAuth } from "./utils";
import {
    PersonToArrResp,
    PersonToResp,
    TPersonToArrResp,
    TPersonToResp,
} from "./dto";

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

export async function getPersonByUuid(uuid: string): Promise<TPersonToResp> {
    return fetchJSONAuth("/person/by-uuid/" + uuid, "GET", PersonToResp);
}

export async function getSelf(): Promise<TPersonToResp> {
    return fetchJSONAuth("/person/self", "GET", PersonToResp);
}

export async function getAllPerson(): Promise<TPersonToArrResp> {
    return fetchJSONAuth("/person", "GET", PersonToArrResp);
}

export async function getPersonByUsername(
    username: string,
): Promise<TPersonToResp> {
    return fetchJSONAuth(
        "/person/by-username/" + username,
        "GET",
        PersonToResp,
    );
}
