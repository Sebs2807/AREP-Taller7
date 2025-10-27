import { UserManager } from "https://cdn.jsdelivr.net/npm/oidc-client-ts/+esm";

const cognitoAuthConfig = {
    authority: "https://cognito-idp.us-east-1.amazonaws.com/us-east-1_z8xIUETwu",
    client_id: "5ioimrqnbse75p8d0evu3c85n0",

    redirect_uri: window.location.origin,
    response_type: "code",
    scope: "openid email profile"
};

export const userManager = new UserManager(cognitoAuthConfig);

export async function signOutRedirect() {
    const logoutUri = window.location.origin;

    const hostedUiDomain = "https://arep.auth.us-east-1.amazoncognito.com";
    window.location.href = `${hostedUiDomain}/logout?client_id=${cognitoAuthConfig.client_id}&logout_uri=${encodeURIComponent(logoutUri)}`;
}

export async function signIn() {
    await userManager.signinRedirect();
}

export async function handleCallback() {
    const user = await userManager.signinCallback();

    localStorage.setItem("token", user.id_token);
    localStorage.setItem("username", user.profile.email);
}
