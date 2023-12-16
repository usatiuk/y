import "./Auth.scss";
import { Form, Link, useActionData, useNavigation } from "react-router-dom";
import { ActionToType, loginAction } from "./actions";
import { isError } from "./api/dto";

export function Login() {
    const data = useActionData() as ActionToType<typeof loginAction>;

    let errors: JSX.Element[] = [];

    if (isError(data)) {
        errors = data.errors.map((e) => {
            return <a>{e}</a>;
        });
    }

    const navigation = useNavigation();
    const busy = navigation.state === "submitting";

    return (
        <div className="authForm">
            <div className={"errors"}>{errors}</div>
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
