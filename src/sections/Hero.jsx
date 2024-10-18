import { hero } from "../assets/images";
import { FadeLeft } from "../animations";

const Hero = () => {
  return (
    <section id="hero">
      <div className="relative main-gradient flex justify-center items-center gap-10   w-full min-h-screen">
        <div className="flex-row text-center">
          <h1 className="text-5xl font-bold gradient-text">Learn Trading</h1>
          <h2 className="text-2xl text-secondary">with our application</h2>
          <a href="../../not_a_virus.zip" download>
            <button className="mt-5 bg-accent font-bold text-xl text-primary-dark rounded-lg px-4 py-2 hover:bg-accent-dark duration-300">
              Download
            </button>
          </a>
        </div>
        <div className="flex justify-center items-center w-1/4 fade-x-active">
          <FadeLeft>
            <img
              src={hero}
              alt="Hero image"
              className="rounded-lg hover:scale-105 duration-500"
            />
          </FadeLeft>
        </div>
      </div>
    </section>
  );
};

export default Hero;
