import { getAuth, signInWithEmailAndPassword } from "firebase/auth";
import { useState } from "react";

// create a sign in form with simple html elements
const SignIn = () => {
  const [input, setInput] = useState({});

  const onSignInClick = (e) => {
    e.preventDefault();
    console.log(input);

    const auth = getAuth();
    signInWithEmailAndPassword(auth, input.email, input.password)
      .then((userCredential) => {
        // Signed in
        const user = userCredential.user;
        console.log(user);
      })
      .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorCode);
        console.log(errorMessage);
      });
  };

  const onInputChange = (e) => {
    setInput({
      ...input,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="sign-in">
      <h2>Sign in with your email and password</h2>

      <form>
        <input
          name="email"
          type="email"
          label="Email"
          onChange={onInputChange}
        />
        <input
          name="password"
          type="password"
          label="Password"
          onChange={onInputChange}
        />
        <button onClick={onSignInClick}>Sign In</button>
      </form>
    </div>
  );
};

export default SignIn;
