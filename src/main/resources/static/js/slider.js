const products = [
  { title: "Smartwatch 1", description: "Monitoruj swoją aktywność, sen i tętno. Wodoodporny do 50m. Posiada GPS i ekran AMOLED.", img: "https://townandcountryremovals.com/wp-content/uploads/2013/10/firefox-logo-200x200.png" },
  { title: "Słuchawki 2", description: "Aktywna redukcja szumów (ANC) i krystalicznie czysty dźwięk. Do 30 godzin pracy na baterii.", img: "https://media.istockphoto.com/id/1419410282/pl/zdj%C4%99cie/cichy-las-na-wiosn%C4%99-z-pi%C4%99knymi-jasnymi-promieniami-s%C5%82onecznymi.webp?s=2048x2048&w=is&k=20&c=gO5I_nFoRBDMLbL5N3H4rmjICaSgmstkH3mtfGDbcOI=" },
  { title: "Kamera 3", description: "Nagrywaj w 4K przy 60 kl./s. Idealna dla vloggerów i twórców treści.", img: "https://cdn.pixabay.com/photo/2022/06/13/14/58/road-7260175_960_720.jpg" },
  { title: "Dron 4", description: "To jest bardzo długi opis, który ma na celu sprawdzenie, czy scrollbar się pojawi. Musi on zawierać wystarczająco dużo tekstu, aby przekroczyć dostępną wysokość w kontenerze .card-body. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", img: "https://cdn.pixabay.com/photo/2017/03/27/13/05/conifers-2178595_960_720.jpg" },
  { title: "Dron 5", description: "Kompaktowy i lekki, z czasem lotu do 30 minut. Świetny dla początkujących. Posiada tryb śledzenia obiektu.", img: "https://cdn.pixabay.com/photo/2020/04/07/13/18/mist-5013325_960_720.jpg" }
];

const track = document.getElementById("track");
let cardSlots = []; 
let active = 0; 
let isAnimating = false;
let touchStartX = 0;
let touchStartY = 0;
let VISIBLE_CARDS; 
let CENTER_INDEX;
let autoPlayTimer = null;
const AUTO_PLAY_DELAY = 1750; 

function getProduct(index) {
    const n = products.length;
    const safeIndex = (index % n + n) % n;
    return products[safeIndex];
}

function renderSlot(el, product) {
  el.innerHTML = `
    <img src="${product.img}" class="card-img-top" alt="${product.title}">
    <div class="card-body">
      <div class="product-title">${product.title}</div>
      <div class="product-description">${product.description}</div>
      <button class="btn-buy">Zobacz produkt</button>
    </div>
  `;
}

function updateSlots() {
  cardSlots.forEach((el, i) => {
    const posIndex = i - CENTER_INDEX; 
    el.style.setProperty('--i', posIndex);
    const product = getProduct(active + posIndex);
    renderSlot(el, product);
  });
}

function animate(direction) {
  if (isAnimating) return;
  isAnimating = true;

  if (direction === 'next') {
    active = (active + 1) % products.length;
    cardSlots.forEach(el => {
      const newPos = parseInt(el.style.getPropertyValue('--i')) - 1;
      el.style.setProperty('--i', newPos);
    });
  } else {
    active = (active - 1 + products.length) % products.length;
    cardSlots.forEach(el => {
      const newPos = parseInt(el.style.getPropertyValue('--i')) + 1;
      el.style.setProperty('--i', newPos);
    });
  }

  const elementToWatch = cardSlots[CENTER_INDEX]; 
  
  elementToWatch.addEventListener('transitionend', () => {
    let slotToReset;
    
    if (direction === 'next') {
      slotToReset = cardSlots.find(el => parseInt(el.style.getPropertyValue('--i')) === -CENTER_INDEX - 1);
      slotToReset.style.transition = 'none'; 
      slotToReset.style.setProperty('--i', CENTER_INDEX); 
      renderSlot(slotToReset, getProduct(active + CENTER_INDEX)); 
    } else {
      slotToReset = cardSlots.find(el => parseInt(el.style.getPropertyValue('--i')) === CENTER_INDEX + 1);
      slotToReset.style.transition = 'none'; 
      slotToReset.style.setProperty('--i', -CENTER_INDEX); 
      renderSlot(slotToReset, getProduct(active - CENTER_INDEX)); 
    }
    
    void slotToReset.offsetWidth;
    slotToReset.style.transition = '';
    isAnimating = false;
  }, { once: true });
}

function handleTouchStart(e) {
  touchStartX = e.touches[0].clientX;
  touchStartY = e.touches[0].clientY;
}

function handleTouchMove(e) {
  if (!touchStartX || !touchStartY) return;
  const deltaX = e.touches[0].clientX - touchStartX;
  const deltaY = e.touches[0].clientY - touchStartY;
  if (Math.abs(deltaX) > Math.abs(deltaY)) e.preventDefault();
}

function handleTouchEnd(e) {
  if (isAnimating || !touchStartX) return;
  const deltaX = e.changedTouches[0].clientX - touchStartX;
  const swipeThreshold = 50; 
  if (deltaX < -swipeThreshold) {
    animate('next');
    startAutoPlay(); 
  } else if (deltaX > swipeThreshold) {
    animate('prev');
    startAutoPlay(); 
  }
  touchStartX = 0;
  touchStartY = 0;
}

function startAutoPlay() {
  if (autoPlayTimer) clearInterval(autoPlayTimer);
  autoPlayTimer = setInterval(() => animate('next'), AUTO_PLAY_DELAY);
}

function stopAutoPlay() {
  if (autoPlayTimer) clearInterval(autoPlayTimer);
}

function init() {
  const sliderContainer = document.getElementById('product-slider-container');
  
  if (!sliderContainer) {
    console.error("Nie można znaleźć kontenera slidera: #product-slider-container");
    return;
  }

  const numProducts = products.length;
  const nextBtn = document.getElementById('nextBtn');
  const prevBtn = document.getElementById('prevBtn');
  const sliderWrap = sliderContainer.querySelector('.slider-wrap'); 

  if (!sliderWrap || !nextBtn || !prevBtn) {
    console.error("Brakujące elementy slidera (wrap, nextBtn lub prevBtn).");
    return;
  }

  if (numProducts === 1) {
    nextBtn.style.display = 'none';
    prevBtn.style.display = 'none';
    VISIBLE_CARDS = 1;
    CENTER_INDEX = 0;
    const slot = document.createElement('div');
    slot.classList.add('card-slot');
    track.appendChild(slot);
    cardSlots.push(slot);
    updateSlots(); 
    return; 
  }

  if (numProducts === 2) {
    VISIBLE_CARDS = 3; 
    sliderContainer.classList.add('slider-mode-2');
  } 
  else if (numProducts >= 3 && numProducts <= 4) {
    VISIBLE_CARDS = 3;
    sliderContainer.classList.add('slider-mode-3');
  } 
  else {
    VISIBLE_CARDS = 5;
    sliderContainer.classList.add('slider-mode-5');
  }
  
  CENTER_INDEX = Math.floor(VISIBLE_CARDS / 2);

  for (let i = 0; i < VISIBLE_CARDS; i++) {
    const slot = document.createElement('div');
    slot.classList.add('card-slot');
    track.appendChild(slot);
    cardSlots.push(slot);
  }

  updateSlots();

  nextBtn.addEventListener('click', () => {
    animate('next');
    startAutoPlay(); 
  });
  prevBtn.addEventListener('click', () => {
    animate('prev');
    startAutoPlay(); 
  });

  track.addEventListener('touchstart', handleTouchStart, { passive: false });
  track.addEventListener('touchmove', handleTouchMove, { passive: false });
  track.addEventListener('touchend', handleTouchEnd);

  sliderWrap.addEventListener('mouseenter', stopAutoPlay);
  sliderWrap.addEventListener('mouseleave', startAutoPlay);

  startAutoPlay();
}

init();