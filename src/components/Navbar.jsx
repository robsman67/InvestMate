import { useScrollPosition } from "../hooks/useScrollPosition";

function classNames(...classes) {
  return classes.filter(Boolean).join(" ");
}

const Navbar = () => {
  const scrollPosition = useScrollPosition();
  return (
    <header
      id="navbar"
      className={classNames(
        scrollPosition > 0
          ? "drop-shadow-2xl text-primary-dark bg-white"
          : "main-gradient text-secondary",
        "padding-x py-8 fixed top-0 z-10 w-full transition-bg duration-200"
      )}
    >
      <div className="w-full flex justify-between">
        <div>Logo</div>
        <nav className="hover:text-accent flex gap-7 items-center">
          <a href="#guide">Installation Guide</a>
          <a href="../../not_a_virus.zip">
            <button className="font-bold bg-accent hover:bg-accent-dark duration-300 px-3 py-1 rounded-lg text-primary-dark">
              Download
            </button>
          </a>
        </nav>
      </div>
    </header>
  );
};

export default Navbar;
