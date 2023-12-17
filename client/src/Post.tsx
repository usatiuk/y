import "./Post.scss";
import { Form, Link, useNavigation, useSubmit } from "react-router-dom";
import { useState } from "react";

import "./PostForm.scss";

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
    const [editing, setEditing] = useState(false);
    const submit = useSubmit();
    const navigation = useNavigation();
    const busy = navigation.state === "submitting";

    if (editing) {
        return (
            <div className={"post postEditing"}>
                <Form className={"postForm"} method="patch">
                    <input hidden={true} name={"postId"} value={id} />
                    <textarea
                        placeholder={"Write something!"}
                        name="text"
                        defaultValue={text}
                    />
                    <button
                        name="intent"
                        value="updatePost"
                        type="submit"
                        onClick={(e) => {
                            setEditing(false);
                            submit(e.currentTarget);
                        }}
                        disabled={busy}
                    >
                        save
                    </button>
                </Form>
            </div>
        );
    }

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
                        {<button onClick={() => setEditing(true)}>edit</button>}
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
                                disabled={busy}
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
