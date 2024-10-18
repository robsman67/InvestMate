import React, { useEffect, useRef, useState } from "react";

const FadeLeft = ({ children }) => {
  const [isVisible, setIsVisible] = useState(false);
  const ref = useRef();

  const handleScroll = () => {
    if (ref.current) {
      const rect = ref.current.getBoundingClientRect();
      setIsVisible(rect.top >= -100 && rect.bottom <= window.innerHeight + 100);
    }
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    handleScroll(); // Initial check

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  return (
    <div ref={ref} className={isVisible ? "fade-x-active" : "fade-left"}>
      {children}
    </div>
  );
};

export default FadeLeft;
