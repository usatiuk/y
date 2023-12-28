import { useLoaderData } from "react-router-dom";
import { LoaderToType, userListLoader } from "./loaders";
import { isError } from "./api/dto";
import { ProfileCard } from "./ProfileCard";
import { useHomeContext } from "./HomeContext";

import "./UserList.scss";

export function UserList() {
    const loaderData = useLoaderData() as LoaderToType<typeof userListLoader>;
    const homeContext = useHomeContext();

    if (!loaderData) {
        return <div>Error</div>;
    }

    const { people, following } = loaderData;
    if (isError(following) || isError(people)) {
        return <div>Error</div>;
    }
    return (
        <div className={"userList"}>
            {people.map((u) => {
                return (
                    <ProfileCard
                        username={u.username}
                        fullName={u.fullName}
                        uuid={u.uuid}
                        key={u.uuid}
                        actions={homeContext.user.uuid != u.uuid}
                        isAdmin={u.isAdmin}
                        alreadyFollowing={following.some(
                            (f) => f.uuid == u.uuid,
                        )}
                    />
                );
            })}
        </div>
    );
}
