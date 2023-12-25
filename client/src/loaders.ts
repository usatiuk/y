import {
    getAllPerson,
    getFollowing,
    getPersonByUsername,
    getSelf,
} from "./api/Person";
import { deleteToken, getToken, getTokenUserUuid } from "./api/utils";
import { redirect } from "react-router-dom";
import { isError } from "./api/dto";
import {
    getPostsByAuthorUsername,
    getPostsByAuthorUuid,
    getPostsByFollowees,
} from "./api/Post";
import { getChat, getMyChats } from "./api/Chat";
import { getMessagesByChat } from "./api/Message";

export type LoaderToType<T extends (...args: any) => any> =
    | Exclude<Awaited<ReturnType<T>>, Response>
    | undefined;

export async function getCheckUserSelf() {
    if (getToken() == null) {
        return redirect("/login");
    }
    const ret = await getSelf();
    if (isError(ret)) {
        deleteToken();
        return redirect("/");
    }
    return ret;
}

export async function homeLoader() {
    return await getCheckUserSelf();
}

export async function userListLoader() {
    return { people: await getAllPerson(), following: await getFollowing() };
}

export async function profileLoader({
    params,
}: {
    params: { username?: string };
}) {
    const selfUuid = getTokenUserUuid();
    if (!selfUuid) return redirect("/");

    const retUser = params.username
        ? await getPersonByUsername(params.username)
        : null;

    if (retUser && !isError(retUser) && retUser.uuid == selfUuid) {
        return redirect("/home/profile");
    }

    const posts = params.username
        ? await getPostsByAuthorUsername(params.username)
        : await getPostsByAuthorUuid(selfUuid);

    if (
        (params.username && !retUser) ||
        retUser instanceof Response ||
        isError(retUser)
    ) {
        return retUser;
    }

    if (isError(posts)) {
        return { user: retUser, posts: null };
    }

    return { user: retUser, posts };
}

export async function feedLoader() {
    return await getPostsByFollowees();
}

export async function chatListLoader() {
    return await getMyChats();
}

export async function newChatLoader() {
    return await getAllPerson();
}

export async function chatLoader({ params }: { params: { id?: number } }) {
    return {
        chat: await getChat(params.id!),
        messages: await getMessagesByChat(params.id!),
    };
}
