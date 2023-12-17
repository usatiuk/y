import { TPostToArr } from "./api/dto";
import { Post } from "./Post";

export function PostList({
    posts,
    selfUuid,
}: {
    posts: TPostToArr | null;
    selfUuid: string;
}) {
    const sortedPosts = posts?.sort((a, b) => b.createdAt - a.createdAt);
    return (
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
                            actions={selfUuid == p.authorUuid}
                        />
                    );
                })}
        </div>
    );
}
