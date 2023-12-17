import { Form, Link, useLoaderData } from "react-router-dom";
import { LoaderToType, profileLoader } from "./loaders";
import { isError } from "./api/dto";
import { Post } from "./Post";
import { useHomeContext } from "./HomeContext";
import { PostList } from "./PostList";

import "./PostForm.scss";
import "./Profile.scss";

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

    return (
        <div className={"profileView"}>
            <div className={"profileInfo"}>
                <span className={"fullName"}>{user.fullName}</span>
                <span className={"username"}>{user.username}</span>
            </div>
            {self && (
                <div className={"newPost"}>
                    <Form className={"postForm"} method="post">
                        <textarea
                            placeholder={"Write something!"}
                            name="text"
                        />
                        <button name="intent" value="post" type="submit">
                            Post
                        </button>
                    </Form>
                </div>
            )}
            <PostList posts={posts} selfUuid={homeContext.user.uuid} />
        </div>
    );
}
