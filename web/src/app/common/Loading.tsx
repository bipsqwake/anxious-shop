import { ClipLoader } from "react-spinners";

const Loading = () => {
  return (
    <div style={{
      height: "100vh",
      width: "100vw",
      display: "flex",
      alignItems: "center",
      justifyContent: "center"
    }}>
      <ClipLoader size={60} color="#36d7b7" />
    </div>
  );
};

export default Loading;