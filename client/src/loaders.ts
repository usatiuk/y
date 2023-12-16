import { getPersonByUsername, getSelf } from "./api/Person";
import { deleteToken, getToken } from "./api/utils";
import { redirect } from "react-router-dom";
import { isError } from "./api/dto";
import { getPosts } from "./api/Post";

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

export async function profileLoader({
    params,
}: {
    params: { username?: string };
}) {
    const user = params.username
        ? await getPersonByUsername(params.username)
        : await getCheckUserSelf();
    if (!user || user instanceof Response || isError(user)) {
        return user;
    }

    const posts = await getPosts(user.uuid);

    if (isError(posts)) {
        return { user, posts: null };
    }

    return { user, posts };
}
