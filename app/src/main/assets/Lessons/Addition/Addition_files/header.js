function isMobileDevice() {
  return window.innerWidth <= 480 || (window.innerWidth > window.innerHeight && window.innerWidth > 480 && window.innerWidth < 992);
}

function sendEventToGtm(eventName, clickText, clickUrl) {
  if(typeof trackToGtm != "undefined" && typeof dataLayer != "undefined"){
      trackToGtm(dataLayer, eventName, eventName, {'Click Text': clickText, 'Click URL': clickUrl});
  }
}

function linkClickHandler(event, element) {
  var headerEle = document.querySelector('.header-wrapper-v2');
  event.stopPropagation();
  const prevEle = headerEle.getElementsByClassName('nav-link-dweb active')[0];
  const prevChild = prevEle?.childNodes[0];
  const currentChild = element.childNodes[0];

  if(element.classList.contains('active')) {
      element.children[1].style.display = 'none';
      element.classList.remove('active');
  }
  else {
      element.children[1].style.display = 'block';
      element.classList.add('active');
  }
  if(prevEle) {
      prevEle.children[1].style.display = 'none';
      prevEle.classList.remove('active');
  }
}

function removeEmptyAnchorTag(){
  const headerEleTweb = document.querySelector('.header-wrapper-v2-tweb');
  var elementList = headerEleTweb.getElementsByClassName('nav-link-a');
  for(var i=0;i < elementList.length; i++){
      if(elementList[i].innerText.length == 0){
          elementList[i].remove();
      }
  }
}

function modalHandler() {
  const treeLevel = {
      activeElementId: '',
      treeLevel: 1
  };
  const modal = document.getElementById("header-modal");
  modal.style.display = 'block';
  document.documentElement.style.overflow = 'hidden';
  document.body.style.overflow = 'hidden';
  sendEventToGtm('interacted_menu_mobile', '', '');
  sessionStorage.setItem('headerV2Data', JSON.stringify(treeLevel));
  removeEmptyAnchorTag();
}

function closePopupHandler() {
  event.stopPropagation();
  const modal = document.getElementById("header-modal");
  modal.style.display = 'none';
  document.documentElement.style.overflow = '';
  document.body.style.overflow = '';
  const headerV2Data = JSON.parse(sessionStorage.getItem('headerV2Data'));
  const activeElementId = headerV2Data && headerV2Data.activeElementId;
  const treeLevel = headerV2Data && headerV2Data.treeLevel;
  const backBtnEle = document.getElementById("modal-back-btn");
  backBtnEle.children[0].style.display = 'none';
  backBtnEle.children[1].textContent = "";

  if(treeLevel == 1) {
      return;
  }
  else if(treeLevel == 2) {
      let ele = document.getElementById(activeElementId);
      ele.children[1].style.display = 'none';
  }
  else if(treeLevel == 3) {
      let ele = document.getElementById(activeElementId);
      ele.children[1].style.display = 'none';
      ele.parentElement.style.display = 'none';
  }
}

function moveToNextLevel(element, hasNav, level) {
  const headerEleTweb = document.querySelector('.header-wrapper-v2-tweb');
  const headerV2Data = {
      activeElementId: element.id,
      treeLevel: level
  };
  sessionStorage.setItem('headerV2Data', JSON.stringify(headerV2Data));
  if(element) {
      element.children[1].style.display = 'block';
      element.parentNode.scrollTop = '0';
  }
  const backBtnEle = document.getElementById("modal-back-btn");
  backBtnEle.children[0].style.display = 'block';
  if(level == 3) {
      const title = element.parentElement.children[0].textContent;
      backBtnEle.children[1].textContent = `Back to ${title}`;
      element.parentElement.style.overflow = 'hidden';
  }
  else {
      backBtnEle.children[1].textContent = 'Main Menu';
  }

  // if any element was already expanded then collapse that
  const prevExpandedElement = headerEleTweb.getElementsByClassName('expanded')[0];
  if(prevExpandedElement) {
      const prevEleId = prevExpandedElement.id;
      const prevChevronIconElement = document.getElementById(`chevron-${prevEleId}`);
      prevExpandedElement.style.display = 'none';
      prevExpandedElement.classList.remove('expanded');
      prevExpandedElement.parentNode.style.paddingBottom = '';
      prevExpandedElement.parentElement.querySelector('.heading').style.color = '';
      prevChevronIconElement.style.transform = 'translateY(-50%) rotate(0deg)';
  }

  // logic to expand first element by default
  const categoryTypeElement = element.querySelector('.category-type');
  if (categoryTypeElement) {
      const categoryLinksElement = categoryTypeElement.querySelector('.category-links');
      const chevronIconElement = categoryTypeElement.querySelector('.chevron-down');
      if (categoryLinksElement) {
          categoryLinksElement.classList.add('expanded');
          categoryLinksElement.style.display = 'flex';
          categoryLinksElement.parentNode.style.paddingBottom = '16px';
          categoryLinksElement.parentElement.querySelector('.heading').style.color = '#484848';
          chevronIconElement.style.transform = 'translateY(-50%) rotate(180deg)';
      }
  }
}

function backMenuHandler(event) {
  event.stopPropagation();
  const activeElementId = JSON.parse(sessionStorage.getItem('headerV2Data')).activeElementId;
  const ele = document.getElementById(activeElementId);
  const treeLevel = JSON.parse(sessionStorage.getItem('headerV2Data')).treeLevel;
  const backBtnEle = document.getElementById("modal-back-btn");
  if(treeLevel == 3) {
      backBtnEle.children[1].textContent = 'Main Menu';
      backBtnEle.children[0].style.display = 'block';
      ele.parentNode.style.overflow = 'auto';
  }
  else {
      backBtnEle.children[1].textContent = '';
      backBtnEle.children[0].style.display = 'none';
  }
  ele.children[1].style.display = 'none';
  const updatedHeaderV2Data = {
      activeElementId: treeLevel == 3 ? ele.parentElement.parentElement.id : ele.id,
      treeLevel: treeLevel - 1
  }
  sessionStorage.setItem('headerV2Data', JSON.stringify(updatedHeaderV2Data));
}

function collapseAndExpandHandler(event, id='') {
  event.stopPropagation();
  const headerEleTweb = document.querySelector('.header-wrapper-v2-tweb');
  const element = document.getElementById(id);
  const chevronIconElement = document.getElementById(`chevron-${id}`);

  const prevExpandedElement = headerEleTweb.getElementsByClassName('expanded')[0];
  if(prevExpandedElement && prevExpandedElement != element) {
      const prevEleId = prevExpandedElement.id;
      const prevChevronIconElement = document.getElementById(`chevron-${prevEleId}`);
      prevExpandedElement.style.display = 'none';
      prevExpandedElement.classList.remove('expanded');
      prevExpandedElement.parentNode.style.paddingBottom = '';
      prevExpandedElement.parentElement.querySelector('.heading').style.color = '';
      prevChevronIconElement.style.transform = 'translateY(-50%) rotate(0deg)';
  }
  if(element && element.style.display == 'flex') {
      element.style.display = 'none';
      element.classList.remove('expanded');
      element.parentNode.style.paddingBottom = '';
      element.parentElement.querySelector('.heading').style.color = '';
      chevronIconElement.style.transform = 'translateY(-50%) rotate(0deg)';
  }
  else {
      element.style.display = 'flex';
      element.classList.add('expanded');
      element.parentNode.style.paddingBottom = '16px';
      element.parentElement.querySelector('.heading').style.color = '#484848';
      chevronIconElement.style.transform = 'translateY(-50%) rotate(180deg)';
  }
}

window.onload = function () {
document.body.addEventListener('click', function(event) {
  event.stopPropagation();
  var headerEle = document.querySelector('.header-wrapper-v2');
  // Check if the clicked element is outside of the dropdown
  const activeEle = headerEle.getElementsByClassName('nav-link-dweb active')[0];
  const anchorEle = activeEle && activeEle.children[0];
  const dropdownLayover = activeEle && activeEle.children[1];
  const dropdown = dropdownLayover && dropdownLayover.children[0];
  if (dropdown && anchorEle && dropdownLayover && !dropdown.contains(event.target) && event.target.id !== anchorEle.id) {
      // Clicked outside of the dropdown, hide it
      dropdownLayover.style.display = 'none';
      activeEle.classList.remove('active');
  }

  // Check if the clicked element is outside of the modal - tweb/mweb
  const modalContentEle = document.getElementById('header-modal-content');
  const menuEle = document.getElementById('hamburger-menu');
  if (modalContentEle && menuEle && !modalContentEle.contains(event.target) && event.target !== menuEle && !event.target.closest('#hamburger-menu')) {
      // Clicked outside of the modal, close it
      closePopupHandler();
  }
}, false);


if(isMobileDevice()) {
  const twebHeaderElement = document.querySelector('.header-wrapper-v2-tweb');
  twebHeaderElement.classList.add('header-wrapper-v2-mweb');
}


setTimeout(() => {
  var headerEle = document.querySelector('.header-wrapper-v2');
  const innerNavEle = headerEle.getElementsByClassName('inner-nav-item');
  const isActiveEle = headerEle.getElementsByClassName('inner-nav-item active')[0];

  if(!isActiveEle) {
      const child = innerNavEle[0].childNodes[1];
      innerNavEle[0].classList.add('active');
      innerNavEle[0].style.color = '#4F52C3';
      innerNavEle[0].style.background = '#FFFFFF';
      innerNavEle[0].style.padding = '10px 12px 10px 24px';
      innerNavEle[0].style.borderRadius = '8px 0 0 8px';
      child.style.display = 'block';
  }

  for(let i = 0; i < innerNavEle.length; i++) {
      innerNavEle[i].addEventListener("mouseover", (event) => {
          const prevEle = headerEle.getElementsByClassName('inner-nav-item active')[0];
          const prevChild = prevEle?.childNodes[1];
          const currentChild = innerNavEle[i].childNodes[1];

          if(prevEle) {
              prevEle.classList.remove('active');
              prevEle.style.color = '#FFFFFF';
              prevEle.style.background = 'transparent';
              prevEle.style.padding = '10px 12px';
              prevEle.style.borderRadius = '8px 0 0 8px';
              prevChild.style.display = 'none';
          }
          innerNavEle[i].classList.add('active');
          innerNavEle[i].style.color = '#4F52C3';
          innerNavEle[i].style.background = '#FFFFFF';
          innerNavEle[i].style.padding = '10px 12px 10px 24px';
          innerNavEle[i].style.borderRadius = '8px 0 0 8px';
          currentChild.style.display = 'block';
      });
  }
}, 100);
};

