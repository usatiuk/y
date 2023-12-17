import { addFollower, removeFollower, signup } from "./api/Person";
import { ActionFunctionArgs, redirect } from "react-router-dom";
import { login } from "./api/Token";
import { isError } from "./api/dto";
import { deleteToken, setToken } from "./api/utils";
import { createPost, deletePost } from "./api/Post";

export type ActionToType<T extends (...args: any) => any> =
    | Exclude<Awaited<ReturnType<T>>, Response>
    | undefined;

export async function loginAction({ request }: ActionFunctionArgs) {
    const formData = await request.formData();
    const ret = await login(
        formData.get("username")!.toString(),
        formData.get("password")!.toString(),
    );

    if (ret && !isError(ret)) {
        setToken(ret.token);
        return redirect("/home");
    }

    return ret;
}

export async function signupAction({ request }: ActionFunctionArgs) {
    const formData = await request.formData();
    const s = await signup(
        formData.get("username")!.toString(),
        formData.get("fullName")!.toString(),
        formData.get("password")!.toString(),
    );

    if (!s || isError(s)) {
        return s;
    }

    // Login if everything's OK
    const ret = await login(
        formData.get("username")!.toString(),
        formData.get("password")!.toString(),
    );

    if (ret && !isError(ret)) {
        setToken(ret.token);
        return redirect("/home");
    }

    return ret;
}

export async function profileSelfAction({ request }: ActionFunctionArgs) {
    const formData = await request.formData();
    const intent = formData.get("intent")!.toString();
    if (intent == "post") {
        return await createPost(formData.get("text")!.toString());
    } else if (intent == "deletePost") {
        return await deletePost(
            parseInt(formData.get("postToDeleteId")!.toString()),
        );
    }
}

export async function homeAction({ request }: ActionFunctionArgs) {
    const formData = await request.formData();
    const intent = formData.get("intent")!.toString();
    if (intent == "logout") {
        deleteToken();
        return redirect("/");
    }
}

export async function userListAction({ request }: ActionFunctionArgs) {
    const formData = await request.formData();
    const intent = formData.get("intent")!.toString();
    if (intent == "follow") {
        return await addFollower(formData.get("uuid")!.toString());
    } else if (intent == "unfollow") {
        return await removeFollower(formData.get("uuid")!.toString());
    }
}
