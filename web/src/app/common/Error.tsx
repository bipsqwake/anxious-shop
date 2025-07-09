const ErrorFallback = () => {
  return (
    <div style={{
      minHeight: "100vh",
      width: "100vw",
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "center",
      color: "#721c24",
      fontFamily: "Arial, sans-serif"
    }}>
      <svg height="64" viewBox="0 0 24 24" width="64" style={{marginBottom: 24}}>
        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
      </svg>
      <h1 style={{ fontSize: 32, margin: 0 }}>Что-то пошло не так</h1>
      <p style={{ fontSize: 18, marginTop: 8, marginBottom: 24, maxWidth: 350, textAlign: "center" }}>
        Приложение временно недоступно. Пожалуйста, попробуйте обновить страницу чуть позже.
      </p>
      <button
        onClick={() => window.location.reload()}
        style={{
          padding: "10px 24px",
          fontSize: 16,
          backgroundColor: "#f44336",
          color: "white",
          border: "none",
          borderRadius: 4,
          cursor: "pointer",
        }}
      >
        Обновить страницу
      </button>
    </div>
  );
};

export default ErrorFallback;