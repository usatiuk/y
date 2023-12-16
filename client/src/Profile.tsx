import "./Profile.scss";
import { Form, Link, useLoaderData } from "react-router-dom";
import { LoaderToType, profileLoader } from "./loaders";
import { isError } from "./api/dto";
import { Post } from "./Post";

export interface IProfileProps {
    self: boolean;
}

export function Profile({ self }: IProfileProps) {
    const loaderData = useLoaderData() as LoaderToType<typeof profileLoader>;

    if (!loaderData || isError(loaderData)) {
        return <div>Error</div>;
    }

    const sortedPosts = loaderData.posts?.sort(
        (a, b) => b.createdAt - a.createdAt,
    );

    return (
        <div className={"profileView"}>
            <div className={"profileInfo"}>
                <span className={"fullName"}>{loaderData.user.fullName}</span>
                <span className={"username"}>{loaderData.user.fullName}</span>
            </div>
            {self && (
                <div className={"newPost"}>
                    <Form method="post">
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
            <div className={"posts"}>
                {sortedPosts &&
                    sortedPosts.map((p) => {
                        const date = new Date(p.createdAt * 1000);
                        return (
                            <Post
                                text={p.text}
                                createdDate={`${date.toUTCString()}`}
                                key={p.id}
                                id={p.id}
                                actions={self}
                            />
                        );
                    })}
            </div>
        </div>
    );
}
