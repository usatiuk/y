import {
    addFollower,
    deleteSelf,
    removeFollower,
    signup,
    updateSelf,
} from "./api/Person";
import { ActionFunctionArgs, redirect } from "react-router-dom";
import { login } from "./api/Token";
import { isError } from "./api/dto";
import { deleteToken, getTokenUserUuid, setToken } from "./api/utils";
import { createPost, deletePost, updatePost } from "./api/Post";
import { createChat, deleteChat, updateChat } from "./api/Chat";
import { addMessagesToChat, deleteMessage, editMessage } from "./api/Message";

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
    } else if (intent == "updatePost") {
        return await updatePost(
            formData.get("text")!.toString(),
            parseInt(formData.get("postId")!.toString()),
        );
    } else if (intent == "user") {
        return await updateSelf(
            formData.get("username")!.toString(),
            formData.get("fullName")!.toString(),
            formData.get("password")!.toString(),
        );
    } else if (intent == "deleteSelf") {
        await deleteSelf();
        deleteToken();
        return redirect("/");
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

export async function newChatAction({ request }: ActionFunctionArgs) {
    const formData = await request.formData();
    const ret = await createChat(formData.get("name")!.toString(), [
        ...formData.getAll("members")!.map((p) => p.toString()),
        getTokenUserUuid()!,
    ]);
    if (ret && !isError(ret)) {
        return redirect("/home/messages/chat/" + ret.id);
    } else return ret;
}

export async function editChatAction({ request }: ActionFunctionArgs) {
    const formData = await request.formData();
    const intent = formData.get("intent")!.toString();
    if (intent == "update") {
        const ret = await updateChat(
            Number(formData.get("chatId")!.toString()),
            formData.get("name")!.toString(),
            [
                ...formData.getAll("members")!.map((p) => p.toString()),
                getTokenUserUuid()!,
            ],
        );

        if (ret && !isError(ret)) {
            return redirect("/home/messages/chat/" + ret.id);
        } else return ret;
    } else if (intent == "delete") {
        const ret = await deleteChat(
            Number(formData.get("chatId")!.toString()),
        );

        if (ret && !isError(ret)) {
            return redirect("/home/messages/chats");
        } else return ret;
    }
}

export async function chatAction({ request }: ActionFunctionArgs) {
    const formData = await request.formData();
    const intent = formData.get("intent")!.toString();
    if (intent == "addMessage") {
        return await addMessagesToChat(
            Number(formData.get("chatId")!.toString()),
            formData.get("text")!.toString(),
        );
    } else if (intent == "deleteMessage") {
        return await deleteMessage(
            Number(formData.get("messageToDeleteId")!.toString()),
        );
    } else if (intent == "updateMessage") {
        return await editMessage(
            Number(formData.get("messageId")!.toString()),
            formData.get("text")!.toString(),
        );
    }
}
