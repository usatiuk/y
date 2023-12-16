import "./Auth.scss";
import { Form, Link, useActionData, useNavigation } from "react-router-dom";
import { loginAction } from "./actions";
import { isError } from "./api/dto";

export function Login() {
    const data = useActionData() as
        | Awaited<ReturnType<typeof loginAction>>
        | undefined;

    if (data && !isError(data)) {
        return <div className="authForm">Login success</div>;
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
                <label htmlFor="password">Password:</label>
                <input type="password" name="password" />
                <button type="submit" disabled={busy}>
                    Login
                </button>
                <Link to="/signup">Signup</Link>
            </Form>
        </div>
    );
}
