import { useLoaderData } from "react-router-dom";
import { LoaderToType, userListLoader } from "./loaders";
import { isError } from "./api/dto";
import { ProfileCard } from "./ProfileCard";

export function UserList() {
    const loaderData = useLoaderData() as LoaderToType<typeof userListLoader>;

    if (!loaderData || isError(loaderData)) {
        return <div>Error</div>;
    }

    return (
        <div>
            {loaderData.map((u) => {
                return (
                    <ProfileCard
                        username={u.username}
                        fullName={u.fullName}
                        key={u.uuid}
                    />
                );
            })}
        </div>
    );
}
