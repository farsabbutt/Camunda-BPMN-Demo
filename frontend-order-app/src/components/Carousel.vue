<template>
    <div id="carouselExampleDark" class="carousel carousel-dark slide" data-bs-ride="carousel">
  <div class="carousel-indicators">
    <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
    <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="1" aria-label="Slide 2"></button>
    <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="2" aria-label="Slide 3"></button>
  </div>
  <div class="carousel-inner">
    <div class="carousel-item active" data-bs-interval="3000">
      <!-- <img src="../assets/bike1.jpg" class="d-block w-100" alt="..."> -->
      <div class="slider-image slider-image-one">
        <div class="carousel-caption d-none d-md-block">
        <h2>Mountain Bicycle</h2>
        <p>One of our finest mountain bicycle for all your mountainous trails!</p>
      </div>
      </div>
    </div>
    <div class="carousel-item" data-bs-interval="3000">
      <!-- <img src="../assets/bike5.jpg" class="d-block w-100" alt="..."> -->
      <div class="slider-image slider-image-two">
      <div class="carousel-caption d-none d-md-block">
        <h2>Hybrid 40000 Bicycle</h2>
        <p>Charge up the best hybrid bicycle for all you adventures!</p>
      </div>
    </div>
    </div>
    <div class="carousel-item" data-bs-interval="3000">
      <!-- <img src="../assets/bike3.jpg" class="d-block w-100" alt="..."> -->
      <div class="slider-image slider-image-three"> 
      <div class="carousel-caption d-none d-md-block">
        <h2>Speed Thriller Electric 147 Bicycle</h2>
        <p>Looking for something thrilling? Thriller 147 is up for grabs!</p>
      </div>
      </div>
    </div>
  </div>
  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
</div>
</template>

<script>
export default {
  name: 'CarouselSlider',
  props: {
    msg: String
  }
}
</script>

<style>

.slider-image {
    width: 100%;
    height: 670px;
    background-size: cover;
}

.slider-image-one {
    background-position-y: -100px;
    background-image: url('../assets/bike1.jpg');
}

.slider-image-two {
    background-image: url('../assets/bike5.jpg');
}

.slider-image-three {
    background-image: url('../assets/bike3.jpg');
}

#carouselExampleDark > div.carousel-inner > div.carousel-item.active > div > div > h2 {
    color: white;
    /* background: rgba(0,0,0,0.5); */
}

#carouselExampleDark > div.carousel-inner > div.carousel-item.active > div > div > p {
    color: white;
    /* background: rgba(0,0,0,0.5); */
}

</style>