import {
    Form,
    useLoaderData,
    useNavigation,
    useSubmit,
} from "react-router-dom";
import { LoaderToType, profileLoader } from "./loaders";
import { isError } from "./api/dto";
import { useHomeContext } from "./HomeContext";
import { PostList } from "./PostList";

import "./PostForm.scss";
import "./Profile.scss";
import { useState } from "react";

export interface IProfileProps {
    self: boolean;
}

export function Profile({ self }: IProfileProps) {
    const loaderData = useLoaderData() as LoaderToType<typeof profileLoader>;

    const homeContext = useHomeContext();

    if (!loaderData || isError(loaderData)) {
        return <div>Error</div>;
    }

    const { posts } = loaderData;

    const user = self ? homeContext.user : loaderData.user;

    if (!user || isError(user)) {
        return <div>Error</div>;
    }
    const navigation = useNavigation();
    const busy = navigation.state === "submitting";
    const [editing, setEditing] = useState(false);
    const submit = useSubmit();

    return (
        <div className={"profileView"}>
            <div className={"profileInfo"}>
                {editing ? (
                    <>
                        <Form className={"userForm"} method="patch">
                            <label htmlFor="fname">Username:</label>
                            <input
                                type="text"
                                defaultValue={user.username}
                                name="username"
                            />
                            <label htmlFor="fname">Full name:</label>
                            <input
                                type="text"
                                defaultValue={user.fullName}
                                name="fullName"
                            />
                            <label htmlFor="password">Password:</label>
                            <input type="password" name="password" />
                            <button
                                name="intent"
                                value="user"
                                type="submit"
                                onClick={(e) => {
                                    setEditing(false);
                                    submit(e.currentTarget);
                                }}
                                disabled={busy}
                            >
                                update
                            </button>
                        </Form>
                    </>
                ) : (
                    <>
                        <span className={"fullName"}>{user.fullName}</span>
                        <span className={"username"}>{user.username}</span>
                        {<button onClick={() => setEditing(true)}>edit</button>}
                        <Form className={"postForm"} method="post">
                            <button
                                name="intent"
                                value="deleteSelf"
                                type="submit"
                                disabled={busy}
                            >
                                delete account
                            </button>
                        </Form>
                    </>
                )}
            </div>
            {self && (
                <div className={"newPost"}>
                    <Form className={"postForm"} method="post">
                        <textarea
                            placeholder={"Write something!"}
                            name="text"
                        />
                        <button
                            name="intent"
                            value="post"
                            type="submit"
                            disabled={busy}
                        >
                            Post
                        </button>
                    </Form>
                </div>
            )}
            <PostList posts={posts} selfUuid={homeContext.user.uuid} />
        </div>
    );
}
