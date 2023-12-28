import "./ProfileCard.scss";
import { Form, Link, useNavigation } from "react-router-dom";
import { useHomeContext } from "./HomeContext";

export function ProfileCard({
    username,
    fullName,
    uuid,
    actions,
    alreadyFollowing,
    isAdmin,
}: {
    username: string;
    fullName: string;
    uuid: string;
    actions: boolean;
    alreadyFollowing: boolean;
    isAdmin: boolean;
}) {
    const homeContext = useHomeContext();

    const navigation = useNavigation();
    const busy = navigation.state === "submitting";

    return (
        <div className={"profileCard"}>
            <div className={"profileInfo"}>
                <Link to={`/home/profile/${username}`} className={"fullName"}>
                    {fullName}
                </Link>
                <Link to={`/home/profile/${username}`} className={"username"}>
                    {username}
                </Link>
            </div>
            <div className={"profileActions"}>
                {actions &&
                    (alreadyFollowing ? (
                        <Form method={"put"}>
                            <input hidden={true} value={uuid} name={"uuid"} />
                            <button
                                type={"submit"}
                                name={"intent"}
                                value={"unfollow"}
                                disabled={busy}
                            >
                                unfollow
                            </button>
                        </Form>
                    ) : (
                        <Form method={"put"}>
                            <input hidden={true} value={uuid} name={"uuid"} />
                            <button
                                type={"submit"}
                                name={"intent"}
                                value={"follow"}
                                disabled={busy}
                            >
                                follow
                            </button>
                        </Form>
                    ))}
                {homeContext.user.isAdmin &&
                    (isAdmin ? (
                        <Form method={"put"}>
                            <input hidden={true} value={uuid} name={"uuid"} />
                            <button
                                type={"submit"}
                                name={"intent"}
                                value={"unadmin"}
                                disabled={busy}
                            >
                                unadmin
                            </button>
                        </Form>
                    ) : (
                        <Form method={"put"}>
                            <input hidden={true} value={uuid} name={"uuid"} />
                            <button
                                type={"submit"}
                                name={"intent"}
                                value={"admin"}
                                disabled={busy}
                            >
                                make admin
                            </button>
                        </Form>
                    ))}
            </div>
        </div>
    );
}
