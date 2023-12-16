import { signup } from "./api/Person";
import { ActionFunctionArgs, redirect } from "react-router-dom";
import { login } from "./api/Token";
import { isError } from "./api/dto";
import { setToken } from "./api/utils";
import { post } from "./api/Post";

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
    return await post(formData.get("text")!.toString());
}
