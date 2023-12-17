import "./Post.scss";
import { Form, Link } from "react-router-dom";

export function Post({
    text,
    createdDate,
    actions,
    authorUsername,
    id,
}: {
    text: string;
    createdDate: string;
    authorUsername: string;
    actions: boolean;
    id: number;
}) {
    return (
        <div className={"post"}>
            <span className={"text"}>{text}</span>
            <div className={"footer"}>
                <div className={"info"}>
                    <span className={"createdDate"}>{createdDate}</span>
                    <Link
                        className={"authorLink"}
                        to={"/home/profile/" + authorUsername}
                    >
                        by {authorUsername}
                    </Link>
                </div>
                {actions && (
                    <div className={"actions"}>
                        <Form method={"delete"}>
                            <input
                                hidden={true}
                                name={"postToDeleteId"}
                                value={id}
                            />
                            <button
                                name="intent"
                                value="deletePost"
                                type={"submit"}
                            >
                                delete
                            </button>
                        </Form>
                    </div>
                )}
            </div>
        </div>
    );
}
