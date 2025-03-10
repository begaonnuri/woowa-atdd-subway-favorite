import { ERROR_MESSAGE, EVENT_TYPE, KEY_TYPE } from "../../utils/constants.js";
import showSnackbar from "../../lib/snackbar/index.js";
import api from "../../api/index.js";

function Login() {
  const $loginButton = document.querySelector("#login-button");
  const $email = document.querySelector("#email");
  const $password = document.querySelector("#password");

  const onLogin = async event => {
    if (event.type === EVENT_TYPE.CLICK || event.key === KEY_TYPE.ENTER) {
      if (!isValid()) {
        showSnackbar(ERROR_MESSAGE.COMMON);
        return;
      }
      try {
        const loginMember = {
          email: $email.value,
          password: $password.value
        };
        const jwt = await api.member.login(loginMember);
        if (jwt) {
          localStorage.setItem("jwt", `${jwt.tokenType} ${jwt.accessToken}`);
          location.href = "/";
          return;
        }
        showSnackbar(ERROR_MESSAGE.COMMON);
      } catch (e) {
        showSnackbar(ERROR_MESSAGE.COMMON);
      }
    }
  };

  const isValid = () => {
    const email = $email.value;
    const password = $password.value;
    return email && password;
  };

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin);
    $password.addEventListener(EVENT_TYPE.KEY_PRESS, onLogin);
  };
}

const login = new Login();
login.init();
