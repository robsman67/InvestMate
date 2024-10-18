import { step_example } from "../assets/images";

const Guide = () => {
  return (
    <section id="guide" className="padding">
      <h1 className="w-full text-center text-4xl underline text-primary-dark">
        Installation Guide
      </h1>
      <div
        id="steps"
        className="pt-10 flex flex-col justify-center items-center gap-10 padding"
      >
        <div id="step-1" className="flex justify-center items-center gap-10">
          <ol className="flex flex-col gap-5">
            <li>
              1. Lorem ipsum dolor sit amet consectetur adipisicing elit. Fugit
              soluta distinctio ratione ex porro quam at amet obcaecati
              inventore maxime! Iure quod dolores recusandae! Reprehenderit esse
              numquam dicta repellat obcaecati.
            </li>
            <li>
              2. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Eaque
              corrupti, illo minus eligendi provident ex consequatur ipsa
              nesciunt. Ea vero perspiciatis sequi autem, sunt quis. Possimus
              maxime itaque impedit perspiciatis!
            </li>
          </ol>
          <img src={step_example} alt="Image of example step" className="" />
        </div>

        <div id="step-2" className="flex justify-center items-center gap-10">
          <img src={step_example} alt="Image of example step" className="" />
          <ol className="flex flex-col gap-5">
            <li>
              3. Lorem ipsum dolor sit amet consectetur adipisicing elit. Fugit
              soluta distinctio ratione ex porro quam at amet obcaecati
              inventore maxime! Iure quod dolores recusandae! Reprehenderit esse
              numquam dicta repellat obcaecati.
            </li>
            <li>
              4. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Eaque
              corrupti, illo minus eligendi provident ex consequatur ipsa
              nesciunt. Ea vero perspiciatis sequi autem, sunt quis. Possimus
              maxime itaque impedit perspiciatis!
            </li>
          </ol>
        </div>
      </div>
    </section>
  );
};

export default Guide;
