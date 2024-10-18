import React from "react";
import { Navbar, Footer } from "./components";
import { Hero, Guide } from "./sections";
function App() {
  return (
    <>
      <Navbar />
      <Hero />
      <Guide />
      <Footer />
    </>
  );
}

export default App;
