import "./Auth.scss";
import { Form, Link, useActionData, useNavigation } from "react-router-dom";
import { signupAction } from "./actions";
import { isError } from "./api/dto";

export function Signup() {
    const data = useActionData() as
        | Awaited<ReturnType<typeof signupAction>>
        | undefined;

    if (data && !isError(data)) {
        return <div className="authForm">Success, now log in!;</div>;
    }

    let errors: JSX.Element[] = [];

    if (data) {
        errors = data.errors.map((e) => {
            return <a>{e}</a>;
        });
    }

    const navigation = useNavigation();
    const busy = navigation.state === "submitting";

    return (
        <div className="authForm">
            {errors}
            <Form method="post">
                <label htmlFor="fname">Username:</label>
                <input type="text" name="username" />
                <label htmlFor="fname">Full name:</label>
                <input type="text" name="fullName" />
                <label htmlFor="password">Password:</label>
                <input type="password" name="password" />
                <button type="submit" disabled={busy}>
                    Signup
                </button>
                <Link to="/login">Login</Link>
            </Form>
        </div>
    );
}
