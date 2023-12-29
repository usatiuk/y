import { useEffect, useState } from "react";
import { deleteUser, getAllPerson } from "./api/Person";
import { getAllMessage } from "./api/Message";
import { getAllPost } from "./api/Post";
import { isError, TMessageTo, TPersonTo, TPostTo } from "./api/dto";

import "./Haters.scss";
import "./Post.scss";
import { getTokenUserUuid } from "./api/utils";

export function Haters() {
    const [data, setData] = useState<
        | null
        | {
              error: false;
              persons: TPersonTo[];
              messages: TMessageTo[];
              posts: TPostTo[];
          }
        | { error: true; what: string }
    >(null);
    const [search, setSearch] = useState<string>("");
    const [deleted, setDeleted] = useState<string[]>([]);

    useEffect(() => {
        let ignore = false;
        const fetchall = async () => {
            return {
                error: false,
                persons: await getAllPerson(),
                messages: await getAllMessage(),
                posts: await getAllPost(),
            };
        };
        fetchall()
            .then((result) => {
                if (isError(result.persons))
                    setData({
                        error: true,
                        what: result.persons.errors.join(" "),
                    });
                else if (isError(result.messages))
                    setData({
                        error: true,
                        what: result.messages.errors.join(" "),
                    });
                else if (isError(result.posts))
                    setData({
                        error: true,
                        what: result.posts.errors.join(" "),
                    });
                else if (!ignore) {
                    setData(
                        result as {
                            error: false;
                            persons: TPersonTo[];
                            messages: TMessageTo[];
                            posts: TPostTo[];
                        },
                    );
                }
            })
            .catch((e) => setData(e));
        return () => {
            ignore = true;
        };
    }, []);

    if (!data || data.error) {
        return (
            <div className={"hatersManagement"}>
                {data ? data.what : "error"}
            </div>
        );
    }

    const foundM = data.messages.filter(
        (m) =>
            m.contents.includes(search) && m.authorUuid != getTokenUserUuid(),
    );
    const foundP = data.posts.filter(
        (p) => p.text.includes(search) && p.authorUuid != getTokenUserUuid(),
    );

    const foundU = new Set<string>();

    const allfound: {
        id: number;
        type: string;
        contents: string;
        author: string;
    }[] = [];

    foundM.forEach((m) => {
        foundU.add(m.authorUuid);
        allfound.push({
            id: m.id,
            type: "message",
            contents: m.contents,
            author: m.authorUsername,
        });
    });
    foundP.forEach((p) => {
        foundU.add(p.authorUuid);
        allfound.push({
            id: p.id,
            type: "post",
            contents: p.text,
            author: p.authorUsername,
        });
    });

    console.log(foundU);

    const foundUArr = Array.from(foundU);

    return (
        <div className={"hatersManagement"}>
            <div className={"tools"}>
                <label htmlFor="whattosearch">Search for:</label>
                <input
                    type="text"
                    name="whattosearch"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
                <button
                    onClick={(e) => {
                        e.preventDefault();

                        const deluser = (who: string[]) => {
                            if (who.length == 0) return;
                            const u = who[0];
                            const rest = who.slice(1);
                            deleteUser(u)
                                .then((e) => {
                                    if (isError(e)) {
                                        setDeleted((old) => [
                                            ...old,
                                            `error deleting user with uuid ${u}: ` +
                                                e.errors.join(" "),
                                        ]);
                                    } else {
                                        setDeleted((old) => [
                                            ...old,
                                            "deleted user with id: " + u,
                                        ]);
                                    }
                                    deluser(rest);
                                })
                                .catch(() => {
                                    setDeleted((old) => [
                                        ...old,
                                        `error deleting user with uuid ${u}`,
                                    ]);
                                });
                        };

                        deluser(foundUArr);
                    }}
                >
                    delete all users
                </button>
                <div>
                    {deleted.map((d) => (
                        <span>{d}</span>
                    ))}
                </div>
            </div>

            <div className={"found"}>
                {allfound.map((m) => (
                    <div key={m.type + +m.id} className={"post"}>
                        <span className={"text"}>{m.contents}</span>
                        <div className={"footer"}>
                            <div className={"info"}>
                                {m.type} by {m.author}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
