import "./assets/style/App.css"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Toaster } from "react-hot-toast";
import AppRouter from './AppRouter';

export default function App() {
  const queryClient = new QueryClient()

  return (
    <QueryClientProvider client={queryClient}>

      <AppRouter />

      <Toaster
        position="top-right"
        reverseOrder={false}
        gutter={8}
        containerClassName=""
        containerStyle={{}}
        toastOptions={{
          // Define default options
          className: '',
          duration: 3000,
          removeDelay: 1000,
          style: {
            background: 'white',
            color: 'black',
          }
        }}
      />
    </QueryClientProvider>
  );
}
