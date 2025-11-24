import doctorsImage from '../../assets/doctorsImage.png'

function HomePage( ) {
  
  return (
     <div className='HomePage'>
      <h1 className="text-4xl font-semibold text-[#BD1A41] tracking-tight leading-tight">
        The Avengers-We fight disease!</h1>
      <img
        src={doctorsImage}
        alt="Avengers Doctors"
        style={{ width: "400px", height: "auto" }}
        className="mx-auto rounded-4xl"
      />
      <h2 className="text-4xl font-semibold text-[#16A842] tracking-tight leading-tight">Please give us your feedback!</h2>
    </div>
  );
  }

export default HomePage;
