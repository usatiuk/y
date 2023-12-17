import { fetchJSON, fetchJSONAuth } from "./utils";
import {
    NoContentToResp,
    PersonToArrResp,
    PersonToResp,
    TNoContentToResp,
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

export async function getFollowing(): Promise<TPersonToArrResp> {
    return fetchJSONAuth("/person/following", "GET", PersonToArrResp);
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

export async function addFollower(uuid: string): Promise<TNoContentToResp> {
    return fetchJSONAuth("/person/following/" + uuid, "PUT", NoContentToResp);
}

export async function removeFollower(uuid: string): Promise<TNoContentToResp> {
    return fetchJSONAuth(
        "/person/following/" + uuid,
        "DELETE",
        NoContentToResp,
    );
}
